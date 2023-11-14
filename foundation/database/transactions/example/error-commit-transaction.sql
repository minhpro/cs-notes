-- Sometime you cannot COMMIT a transaction cause by unrecoverable errors in it.
-- Should run with `psql -U postgres -d forumdb` to see ROLLBACK action
BEGIN;
UPDATE tags SET tag = uppr( tag );
COMMIT;