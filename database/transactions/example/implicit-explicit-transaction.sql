-- Get current transaction id

SELECT current_time, txid_current();

--Get created by transactions

SELECT xmin, * FROM categories;

SELECT xmin, * FROM tags;

-- Comparing implicit and explicit transactions
INSERT INTO tags( tag ) VALUES( 'linux' );
INSERT INTO tags( tag ) VALUES( 'BSD' );
INSERT INTO tags( tag ) VALUES( 'Java' );

-- explicit transactions
BEGIN;
INSERT INTO tags( tag ) VALUES( 'PHP' );
INSERT INTO tags( tag ) VALUES( 'C#' );
COMMIT;

