package pl.piomin.services.stocktrader.model.providers.twelvedata;

import java.util.List;
import java.util.Objects;

public class TimeSeriesResponse {
    private MetaData meta;
    private List<TimeSeriesData> values;
    private String status;
    private String message;

    public TimeSeriesResponse() {
    }

    public TimeSeriesResponse(MetaData meta, List<TimeSeriesData> values, String status, String message) {
        this.meta = meta;
        this.values = values;
        this.status = status;
        this.message = message;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }


    public List<TimeSeriesData> getValues() {
        return values;
    }

    public void setValues(List<TimeSeriesData> values) {
        this.values = values;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSeriesResponse that = (TimeSeriesResponse) o;
        return Objects.equals(meta, that.meta) && 
               Objects.equals(values, that.values) && 
               Objects.equals(status, that.status) && 
               Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, values, status, message);
    }

    @Override
    public String toString() {
        return "TimeSeriesResponse{" +
                "meta=" + meta +
                ", values=" + values +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
