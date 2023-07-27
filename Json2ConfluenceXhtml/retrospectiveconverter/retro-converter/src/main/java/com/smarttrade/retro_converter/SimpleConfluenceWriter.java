
package com.smarttrade.retro_converter;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.security.auth.login.CredentialNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.api.model.content.Content;
import com.atlassian.confluence.api.model.content.Content.ContentBuilder;
import com.atlassian.confluence.api.model.content.ContentBody;
import com.atlassian.confluence.api.model.content.ContentRepresentation;
import com.atlassian.confluence.api.model.content.ContentStatus;
import com.atlassian.confluence.api.model.content.ContentType;
import com.atlassian.confluence.api.model.content.Label;
import com.atlassian.confluence.api.model.content.Space;
import com.atlassian.confluence.api.model.content.id.ContentId;
import com.atlassian.confluence.rest.client.RemoteContentBodyConversionService;
import com.atlassian.confluence.rest.client.RemoteContentBodyConversionServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentDraftService;
import com.atlassian.confluence.rest.client.RemoteContentDraftServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentLabelService;
import com.atlassian.confluence.rest.client.RemoteContentLabelServiceImpl;
import com.atlassian.confluence.rest.client.RemoteContentService;
import com.atlassian.confluence.rest.client.RemoteContentServiceImpl;
import com.atlassian.confluence.rest.client.RemoteSpaceService;
import com.atlassian.confluence.rest.client.RemoteSpaceService.RemoteSpaceFinder;
import com.atlassian.confluence.rest.client.RemoteSpaceServiceImpl;
import com.atlassian.confluence.rest.client.authentication.AuthenticatedWebResourceProvider;
import com.google.common.util.concurrent.AbstractListeningExecutorService;
import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * Project Name : RetroSpective Converter for Confluence<br>
 * Author : @lsauvaire<br>
 * Author Name : Lino Sauvaire( recovery of Maxime Bernard )
 */
public class SimpleConfluenceWriter {

    /**
     * The logger used
     */
    protected static final Logger LOG = LoggerFactory.getLogger(SimpleConfluenceWriter.class);

    /**
     * The parameters to use to communicate with Confluence
     */
    private final ConfluenceParams params;

    /**
     * The service used to interact with Space on Confluence
     */
    private final RemoteSpaceService remoteSpaceService;

    /**
     * The service used to interact with Content (pages) on Confluence
     */
    private final RemoteContentService remoteContentService;

    /**
     * The service used to convert body format on Confluence
     */
    private final RemoteContentBodyConversionService remoteContentBodyConversionService;

    /**
     * The service used to manage Draft on Confluence
     */
    private final RemoteContentDraftService remoteContentDraftService;

    /**
     * The service used to manage labels on Page
     */
    private final RemoteContentLabelService remoteContentLabelService;

    /**
     * ConfluenceWriter constructor
     *
     * @param params
     *            The ConfluenceParams used to communicate with Confluence
     * @throws CredentialNotFoundException
     */
    public SimpleConfluenceWriter(final ConfluenceParams params) {
        if (StringUtils.isBlank(params.getLogin())) throw new IllegalArgumentException();
        if (StringUtils.isBlank(params.getPassword())) throw new IllegalArgumentException();
        if (StringUtils.isBlank(params.getParentUrl())) throw new IllegalArgumentException();
        if (StringUtils.isBlank(params.getUrl())) throw new IllegalArgumentException();
        if (StringUtils.isBlank(params.getSpaceKey())) throw new IllegalArgumentException();

        this.params = params;

        // Set the user login and password to interact with Confluence //
        final AuthenticatedWebResourceProvider provider = AuthenticatedWebResourceProvider.createWithNewClient(params.getUrl());
        provider.setAuthContext(params.getLogin(), params.getPassword().toCharArray());

        final ListeningExecutorService executor = new ConfluenceListeningExecutorService();

        // Create the remote Space service, using the provider and executor //
        remoteSpaceService = new RemoteSpaceServiceImpl(provider, executor);

        // Create the remote Content (page) service, using the provider and executor //
        remoteContentService = new RemoteContentServiceImpl(provider, executor);

        // Create the remote body conversion service, using the provider and executor //
        remoteContentBodyConversionService = new RemoteContentBodyConversionServiceImpl(provider, executor);

        // Create the remote draft service, using the provider and executor //
        remoteContentDraftService = new RemoteContentDraftServiceImpl(provider, executor);

        // Create the remote label service, using the provider and executor //
        remoteContentLabelService = new RemoteContentLabelServiceImpl(provider, executor);
    }

    /**
     * Find a page (Content) on Confluence from its URL. This method modify the space key stored in the parameters if different of the space key of the page parent URL.
     *
     * @return The page, or null if no page was found
     */
    private Content findPageParent() {
        if (params.getParentUrl() == null || params.getParentUrl().length() <= params.getUrl().length()) return null;
        if (!params.getParentUrl().startsWith(params.getUrl())) return null;

        Content pageParent = null;

        // Removes the unnecessary part of the URL and split it //
        final String[] url = params.getParentUrl().replaceFirst(params.getUrl() + "/?", "").split("/");

        // If the page title contains forbidden characters, the URL have the pattern
        // <params.url>/pages/viewpage.action?pageId=1900546 //
        switch (url[0]) {
            case "pages":
                // Retrieves the page ID //
                final String pageParentId = url[1].replaceFirst("viewpage.action\\?pageId=", "");
                final ContentId contentId = ContentId.deserialise(pageParentId);

                try {
                    pageParent = remoteContentService.find().withId(contentId).fetchCompletionStage().toCompletableFuture().get().orElse(null);
                } catch (InterruptedException | ExecutionException exception) {
                    LOG.error("Unable to find the Confluence page parent frim its id :", exception);
                }
                break;
            // If the page do not contains forbidden characters, the URL have the pattern
            // <params.url>/display/support/Page+Title //
            case "display":
                params.setSpaceKey(url[1]);
                final Space space = findSpace();
                final String title = url[2].replaceAll("[+]", " ");

                try {
                    if (space != null) {
                        pageParent = remoteContentService.find().withSpace(space).withTitle(title).fetchCompletionStage().toCompletableFuture().get()
                                .orElse(null);
                    } else {
                        pageParent = remoteContentService.find().withTitle(title).fetchCompletionStage().toCompletableFuture().get().orElse(null);
                    }
                } catch (InterruptedException | ExecutionException exception) {
                    LOG.error("Unable to find the Confluence page parent frim its id :", exception);
                }
                break;
            default:
                return null;
        }

        return pageParent;
    }

    /**
     * Find a Space in Confluence from its space key
     *
     * @return The Space found, or null if no Space was found
     */
    private Space findSpace() {
        Space space = null;

        final RemoteSpaceFinder rsf = remoteSpaceService.find().withKeys(params.getSpaceKey());
        try {
            space = rsf.fetchCompletionStage().toCompletableFuture().get().orElse(null);
            if (space != null) System.out.println("Space " + space.getName() + " (id = " + space.getId() + ", key = " + space.getKey() + ") found");
            else System.err.println("No Space found correspondig to the key " + params.getSpaceKey() + " !");
        } catch (InterruptedException | ExecutionException exception) {
            LOG.error("Unable to find the space on Confluence :", exception);
        }

        return space;
    }

    /**
     * Convert content from a ContentRepresentation to another
     *
     * @param contentToConvert
     *            The content to convert in the new ContentRepresentation
     * @param oldContentRepresentation
     *            The actual ContentRepresentation of the content to convert
     * @param newContentRepresentation
     *            The ContentRepresentation in which convert the content
     * @return The content in the new ContentRepresentation
     */

    /**
     * Function used to push a Draft on Confluence. The draft will contain the content of the ConfluencePage
     * 
     * @param title
     * @param string
     *            The PageConfluence to push on Confluence as a draft
     */
    public void pushDraft(final String data, String title) throws ExecutionException, InterruptedException, TimeoutException {
        // Retrieves the Confluence page parent and its space key if present in the URL
        // //
        final Content pageParent = findPageParent();

        // Creates a new body for the page //
        final ContentBody contentBody = ContentBody.contentBodyBuilder().representation(ContentRepresentation.STORAGE).value(data).build();

        // Creates a new page (DRAFT and not CURRENT, so we can check and edit it if
        // needed before final publication //

        boolean draftPublishOrNot = false;
        try {
            System.out.println("Do you want to publish directly ? ( true / false )");
            final Scanner n = new Scanner(System.in);
            draftPublishOrNot = n.nextBoolean();
            final ConfluenceParams param = new ConfluenceParams();
        } catch (final InputMismatchException e) {
            System.out.println("Invalid Input");
            return;
        }

        final ContentBuilder draftContentBuilder = Content.builder(ContentType.PAGE).status(draftPublishOrNot ? ContentStatus.CURRENT : ContentStatus.DRAFT)
                .title(title).body(contentBody).space(findSpace());
        if (pageParent != null) draftContentBuilder.parent(pageParent);
        final Content draftContent = draftContentBuilder.build();

        // Push the content in the user draft. (or directly as a final page if status =
        // CURRENT) //
        final Content content = remoteContentDraftService.publishNewDraftCompletionStage(draftContent).toCompletableFuture().get(1, TimeUnit.MINUTES);
        final Label label = Label.builder("retrospective").build();
        final Iterable<Label> promise = remoteContentLabelService.addLabelsCompletionStage(content.getId(), Set.of(label)).toCompletableFuture().get(1,
                TimeUnit.MINUTES);
        for (final Label l : promise) {
            System.out.println("Label " + l + " added to draft");
        }
        // Displays the shared draft link //
        try {
            final ContentId contentID = content.getId();
            final String id = String.valueOf(contentID.asLong());
            String url = params.getUrl().endsWith("/") ? params.getUrl() : params.getUrl() + "/";
            url += "pages/resumedraft.action?draftId=" + id;

            System.out.println(draftPublishOrNot ? "Publish link :" + url : "Draft link : " + url);
        } catch (final Exception exception) {
            LOG.error("Cannot find the ID to generate the draft link : ", exception);
        }
    }

    public void createDraft(String retrospective) {
        final Content pageParent = findPageParent();
        // final ContentBody contentBody = ContentBody.contentBodyBuilder().representation(ContentRepresentation.STORAGE).value(retrospective).build();
        final ContentBody contentBody = pageParent.getBody().get(ContentRepresentation.STORAGE);
        System.out.println(contentBody);

    }

    /**
     * Project Name : RetroSpective Converter for Confluence<br>
     * Author : @lsauvaire<br>
     * Author Name : Lino Sauvaire( recovery of Maxime Bernard )
     * <p>
     * Inner class used to execute order on Confluence
     */
    protected class ConfluenceListeningExecutorService extends AbstractListeningExecutorService {

        @Override
        public void execute(final Runnable command) {
            LOG.info("method \"void execute(final Runnable command)\" called");
            command.run();
        }

        @Override
        public List<Runnable> shutdownNow() {
            LOG.info("method \"List<Runnable> shutdownNow()\" called. Not implemented yet...");
            return null;
        }

        @Override
        public void shutdown() {
            LOG.info("method \"void shutdown\" called. Not implemented yet...");
        }

        @Override
        public boolean isTerminated() {
            LOG.info("method \"boolean isTerminated()\" called. Not implemented yet...");
            return false;
        }

        @Override
        public boolean isShutdown() {
            LOG.info("method \"boolean isShutdown()\" called. Not implemented yet...");
            return false;
        }

        @Override
        public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            LOG.info("method \"boolean awaitTermination(final long timeout, final TimeUnit unit)\" called. Not implemented yet...");
            return false;
        }
    }
}
