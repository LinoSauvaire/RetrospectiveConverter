# RetrospectiveConverter
 For Utilisation :
 - You have to put your confluence user and password in the "converter_required.cfg" file OR if in your gradle.properties File (C:\Users\YOUR_USER\.gradle\gradlle.properties)  you have already type your Confluence user and password you don't have to touch the confluence user and password section in the file "converter_required.cfg" you have to change the SpaceKey(click on Space in confluence, right click on the space of you want to go, paste the link in new tab and in the link take the text after /display/SPACEKEY/.... just take the SpaceKey) , The ParentUrl (once in the right space, go to the section where you want to post your retrospective and copy the link of where you want to post your retrospective) 
 - You just have to take yours JsonFile and put in in the right directory ( src/bin/main/resources/json/YourFiles.json
 - And you just have to go to main, change the name of the JsonFile (Lines 33 ) 


