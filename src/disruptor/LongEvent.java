package disruptor;

/**
 * @author HongTaiwei
 * @date 2021/8/10
 */
public class LongEvent {
    private Long data;

    public void setData(Long data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "data=" + data +
                '}';
    }
}
