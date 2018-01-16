# warehouse-work-app
Simple app for warehouse order picking

Native Android + Retrofit2 for API calling.  
  
1. Fill login form.  
2. Validate login form.  
3. Pass to MainActivity.  
4. MainActivity contains a list of invoices, refresh and logout buttons.  
5. List of invoices is a ListView with adaptor, refresh every application login or refresh-button click. 
6. Each invoice has detailed info from server.
7. You can scan QR-code for picking items by invoice's list.
8. When all items are done, send message to server with invocie completion.

All data is taken from server via API(Retrofit2).  
Authentication on server is made by secure cookie (user id).  

[![Watch the video](123)](https://youtu.be/FUKUDUJGlwI)
