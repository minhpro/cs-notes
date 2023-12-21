## Django Models

Have a look at the Model class under `django/db/models.py`. There the class attributes are turned to instance attributes via something like

`setattr(self, field.attname, val)`

One might recommend the whole file (`ModelBase` and `Model` class) as an excellent hands-on example on metaclasses

## Print raw SQL logging

`settings.py` file

```py
LOGGING = {
    'version': 1,
    'filters': {
        'require_debug_true': {
            '()': 'django.utils.log.RequireDebugTrue',
        }
    },
    'handlers': {
        'console': {
            'level': 'DEBUG',
            'filters': ['require_debug_true'],
            'class': 'logging.StreamHandler',
        }
    },
    'loggers': {
        'django.db.backends': {
            'level': 'DEBUG',
            'handlers': ['console'],
        }
    }
}
```