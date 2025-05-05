# Search LIKE with special characters

Suppose, you want to search **LIKE** with special characters (%, _, ', "), how to pass them to SQL **LIKE** clause?

Solution:
    - Escape these special characters.

```Java
String searchName = "%abc"; // want to search with all names start with %abc
String queryStr = """
    SELECT
        u.id,
        u.name
    FROM 
        user u
    WHERE
        u.name LIKE :searchName
""";

Query query = entityManager.createQuery(queryStr);
String escapedSearchName = QueryHelper.escapeCharacterLike(searchName);
query.setParameter("searchName", escapedSearchName + "%"); // Pass input value through prepared statement's parameter to prevent SQL injection errors.
```

Here is the `QueryHelper.escapeCharacterLike()` utility function.

```Java
/**
 * Escape special characters before passing parameter's values to the SQL `LIKE` clause
 * to support search like with special characters.
 * E.g., "%abc" -> "\%abc"
 * @param searchStr a String to search in LIKE clause which may contains special characters.
 * @return an escaped string.
 */
public static String escapeCharacterLike(String searchStr) {
    return searchStr.replace("'", "\\'")
            .replace("\"", "\\\"")
            .replace("%", "\\%")
            .replace("_", "\\_");
}
```

# Clear the confusion about the `escapeCharacterLike` function

This utility function `QueryHelper.escapeCharacterLike()` doesn't affect to SQL injection problem. It's purpose is only escaping the special character in the parameter before passing it to the SQL **LIKE** clause.

To prevent SQL injection error, passing the input value through the parameter (prepared statement), e.g., `query.setParameter("searchName", escapedSearchName + "%");`.