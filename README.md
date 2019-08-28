# warehouse-work-app
Simple app for warehouse order picking

Native Android + Retrofit2 for API calling.  
  
1. Fill login form.  
2. Validate login form.  
3. Pass to MainActivity.  
4. MainActivity contains a list of Items, refresh and logout buttons.
5. List of Items is a ListView with adaptor, refresh every application login or refresh-button click.
6. Each Item has detailed info from server.
7. You can scan QR-code for picking items by Item's list.
8. When all items are done, send message to server with invocie completion.

All data is taken from server via API(Retrofit2).  
Authentication on server is made by secure cookie (user id).  

[![Watch this](https://i.ytimg.com/vi/FUKUDUJGlwI/2.jpg?time=1516107234119)](https://youtu.be/FUKUDUJGlwI)
