## Datetime in MySQL

https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-time-instants.html

### TIMESTAMP
TIMESTAMP is designed to store instants. 

**Time zone**

Any inserted values are converted from the session's time zone to Coordinated Universal Time (UTC) when stored, and converted back to the session's time zone when retrieved

**Supported values**

TIMESTAMP data type can hold values between '1970-01-01 00:00:01' (UTC) and '2038-01-19 03:14:07' (UTC)

### DATETIME

A date and time combination

**Time zone**

Any inserted values are stored as-is, so no automatic time zone conversions are performed.

### Best practice

Using UTC to store datetime. Present in local time zones as expected by the user (UI, Web, Smartphone)

**Quering with time zone presentation**

Assume that timzone is UTC+9

SET @zone_offset = '09:00';

SELECT 
    ADDTIME(datetime_field, @zone_offet) as timezone_present, 
    datetime_field as origin_value 
FROM example_table
WHERE 
    ADDTIME(datetime_field, @zone_offet) BETWEEN '2021-10-10 19:00:01'
    AND '2021-10-11 19:00:01';

If @zone_offset is negative, using SUBTIME function

**Inserting with time zone presentation**

SET @zone_offset = '09:00';

INSERT INTO example_table(datetime_field)
VALUES (SUBTIME('2021-10-10 19:00:01', @zone_offset));

If @zone_offset is negative, using ADDTIME function

**Interaction from application**

The application should use UTC times to interact with the database

The API should receive UTC input (unix epoch for example) or string datetime presentation with a specific timezone. 

**Date functions**

Some date functions such as the day truncate (return day with zero time part), first day of week, month, or year are usually in a time zone context. When using these functions with UTC time, must provide a timezone (or using the default timezone).

For example:
* timezone is UTC+9
* value = UTC time (instant time) is 1633802077  (2021-10-09 17:54:37 UTC, 2021-10-10 2:54:37 in UTC+9)

dayTruncate(value, timezone) should return 1633791600 (2021-10-09 15:00:00 UTC, 2021-10-09 24:00:00 in UTC+9)

