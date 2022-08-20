-- The transaction is aborted if there is any errors, ROLLBACK command is issued automatically
BEGIN;
INSERT INTO tags( tag ) VALUES( 'C++' );
INSERT INTO tags( tag ) VALUES( JavaScript );
INSERT INTO tags( tag ) VALUES( 'Ocaml' );
COMMIT;