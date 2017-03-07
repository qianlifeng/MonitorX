package monitorx.controller.api;

public class APIResponse {
    boolean success;
    String message;
    Object data;

    public static APIResponse buildSuccessResponse() {
        return buildSuccessResponse("", null);
    }

    public static APIResponse buildSuccessResponse(Object data) {
        return buildSuccessResponse("", data);
    }

    public static APIResponse buildSuccessResponse(String message, Object data) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static APIResponse buildErrorResponse(String message) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
