from django.urls import path, include

import django.contrib.auth.urls as default_auth

from . import views

app_name = 'accounts'
urlpatterns = [
  # Include the default auth urls
  path('', include(default_auth)),
  # Registration page.
  path('register/', views.register, name='register'),
]