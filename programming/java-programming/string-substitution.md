## String substitution using Apache Common Text

Class `org.apache.commons.text.StringSubstitutor`

Docs: https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/StringSubstitutor.html

Substitues variables within a string by values.

The default definition of a variable is `${variableName}`. Variable values are typically resolved from a map, but could also be resolved from system properties, or by supplying a custom variable resolver.

For example:

```Java
// Build map
 Map<String, String> valuesMap = new HashMap<>();
 valuesMap.put("animal", "quick brown fox");
 valuesMap.put("target", "lazy dog");
 String templateString = "The ${animal} jumped over the ${target} ${undefined.number:-1234567890} times.";

 // Build StringSubstitutor
 StringSubstitutor sub = new StringSubstitutor(valuesMap);

 // Replace
 String resolvedString = sub.replace(templateString);
```
 
yielding:

 "The quick brown fox jumped over the lazy dog 1234567890 times."

