OAuth2 là giao thức (một chuẩn) dùng để chia sẻ protected `resource` (thông tin, dịch vụ) trên server cho app bên thứ 3 mà không cần chia sẻ thông tin đăng nhập (e,g, username và password).

Give the client access to the protected resource on behalf of
the resource owner

0. Client Registration

Client Registration
client_id 	utfAiscOlmNoU8gs7aoSKGjT
client_secret 	faImEMALGjUbNguMuNeytBIXw_g9vxxTw8b7H7NHKXDjjS34
User Account
login 	hurt-giraffe@example.com
password 	Nervous-Seal-70

1. Build the authorization URL and redirect the user to the authorization server

https://authorization-server.com/authorize?
  response_type=code
  &client_id=utfAiscOlmNoU8gs7aoSKGjT
  &redirect_uri=https://oauth.com/playground/authorization-code.html
  &scope=photo+offline_access
  &state=7soaCVBCoyaueG4c

Authorization Server will show login page for user enter password and then prompt a consent page to ask user grant permission to client to access resource


2. Verify the state parameter

The user was redirected back to the client, and you'll notice a few additional query parameters in the URL:

?state=7soaCVBCoyaueG4c&code=VOrUtVraqdEEt5UbS8ATf1kGUMDTgJj5OxGEvb95UB3g4prw

You need to first verify that the state parameter matches the value stored in this user's session so that you protect against CSRF attacks.

Depending on how you've stored the state parameter (in a cookie, session, or some other way), verify that it matches the state that you originally included in step 1. Previously, we had stored the state in a cookie for this demo.

Does the state stored by the client (7soaCVBCoyaueG4c) match the state in the redirect (7soaCVBCoyaueG4c)?



3. Exchange the Authorization Code

Now you're ready to exchange the authorization code for an access token.

The client builds a POST request to the token endpoint with the following parameters:

POST https://authorization-server.com/token

grant_type=authorization_code
&client_id=utfAiscOlmNoU8gs7aoSKGjT
&client_secret=faImEMALGjUbNguMuNeytBIXw_g9vxxTw8b7H7NHKXDjjS34
&redirect_uri=https://oauth.com/playground/authorization-code.html
&code=VOrUtVraqdEEt5UbS8ATf1kGUMDTgJj5OxGEvb95UB3g4prw

Note that the client's credentials are included in the POST body in this example. Other authorization servers may require that the credentials are sent as a HTTP Basic Authentication header.



Token Endpoint Response

Here's the response from the token endpoint! The response includes the access token and refresh token.

{
  "token_type": "Bearer",
  "expires_in": 86400,
  "access_token": "aHuhi-mkyBOAJiWNwY7xm8tGw2mV4g-FUFXnNgalWcB7JlNkQqLMkyRwQiDhO3M_vI1Q_dyT",
  "scope": "photo offline_access",
  "refresh_token": "iY6Q2J1sZTPMo-75DrOtjZox"
}

Great! Now your application has an access token, and can use it to make API requests on behalf of the user