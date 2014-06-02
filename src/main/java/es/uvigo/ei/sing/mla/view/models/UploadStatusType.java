package es.uvigo.ei.sing.mla.view.models;

enum UploadStatusType {
	STOPPED(""),
	IN_PROGRESS("Uploading in progress, please wait..."),
	FINISHED("Uploading finished"),
	ERROR("Invalid format, filetype must be ZIP or TAR");

	private final String message;

	private UploadStatusType(final String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
