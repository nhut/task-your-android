package fi.donhut.taskyourandroid.object;

public class KeyValueData<T> {
	
	private String text;
	private T value;
	private long id = hashCode();
	private boolean checked = false;
	
	public KeyValueData(String spinnerText, T value ) {
        this.text = spinnerText;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public T getValue() {
        return value;
    }

    public String toString() {
        return text;
    }

	public long getId() {
		return id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
}
