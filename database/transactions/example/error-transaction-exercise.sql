-- Exercise: make an error transaction
-- Add a constraint that check length of tags must greater than 2
-- Create a transaction that violates the above constraint

-- Constraint
ALTER TABLE tags ADD CONSTRAINT constraint_tag_length CHECK ( length(tag) >= 2);

-- Transaction
BEGIN;
INSERT INTO tags(tag) VALUES('C');
INSERT INTO tags(tag) VALUES('C++');
COMMIT;
