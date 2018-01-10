# login-screen-application
Simple login screen app

Native Android + Retrofit2 for API calling.  
  
1. Fill login form.  
2. Validate login form.  
3. Pass to MainActivity.  
4. MainActivity contains a list of invoices, refresh and logout buttons.  
5. List of invoices is a ListView with adaptor, refresh every application login or refresh-button click.  

All data is taken form server via API(Retrofit2).  
Authentication on server is made by secure cookie (user id).  
