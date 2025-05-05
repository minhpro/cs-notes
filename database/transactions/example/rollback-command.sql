-- rollback transaction
BEGIN;
UPDATE tags SET tag = upper(tag);
SELECT tag FROM tags;
ROLLBACK;

SELECT tag FROM tags;
