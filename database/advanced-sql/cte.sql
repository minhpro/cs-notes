employee(id, name, manager_id)

manager_id is the id of the manager.

With an employee, write a query to return all his/her subordinates

WITH RECURSIVE manager AS (
        SELECT id, name, manager_id, 1 as level
        FROM employee
        WHERE name = 'Nguyen Van A'
    UNION ALL
        SELECT id, name, manager_id, manager.level + 1 as level
        FROM employee e
        JOIN manager m ON e.manager_id = m.id
)
SELECT * from manager;