In order to enable Mockito annotation (such as @Spy, @Mock, … ) – we need to do one of the following:

* Call the method MockitoAnnotations.initMocks(this) to initialize annotated fields
* Use the built-in runner @RunWith(MockitoJUnitRunner.class)
