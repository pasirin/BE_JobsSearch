package com.example.JobsSearch.payload.Response;

/**
 * Chứa các thông tin cần để trao đổi giữa Service và Controller
 */
public class ResponseObject {
    private Boolean status = false;
    private String message;
    private Object data;

    public ResponseObject() {
    }

    public ResponseObject(Boolean status) {
        this.status = status;
    }

    public ResponseObject(String message) {
        this.message = message;
    }

    public ResponseObject(Boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Khởi tạo một ResponseObject với status
     *
     * @param status định nghĩa request chạy được hay không được
     * @return trả về một ResponseObject để chain các câu lệnh tiếp
     */
    public static ResponseObject status(Boolean status) {
        return new ResponseObject(status);
    }

    /**
     * Khởi tạo một ResponseObject với message
     *
     * @param message Gắn ResponseObject với message
     * @return trả về một ResponseObject để chain các câu lệnh tiếp
     */
    public static ResponseObject message(String message) {
        return new ResponseObject(message);
    }

    public static ResponseObject ok() {
        return new ResponseObject().setStatus(true);
    }

    public Boolean getStatus() {
        return status;
    }

    public ResponseObject setStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseObject setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseObject setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
