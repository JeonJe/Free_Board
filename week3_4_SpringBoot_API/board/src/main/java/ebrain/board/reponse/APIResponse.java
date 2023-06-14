package ebrain.board.reponse;

import lombok.Data;

import java.util.List;

@Data
public class APIResponse {
    private boolean success;
    private String message;
    private Object data;
}