package my_group.web;

public record ResponseData<T>(T data, ErrorData error)
{
    public static ResponseData<Void> EMPTY_RESPONSE = new ResponseData<>(null, null);

    public ResponseData(T data) { this(data, null); }

    public ResponseData(ErrorData error) { this(null, error); }
}
