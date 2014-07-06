package es.uvigo.ei.sing.mla.util;

import java.util.ArrayList;
import java.util.List;

public enum CellNameType {
	NUMERICAL, LOWERCASE, UPPERCASE;

	public String indexToLabel(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("index can't be null");

		return createLabels(index)[index - 1];
	}

	public String[] createLabels(int num) {
		final List<String> titles = new ArrayList<>();

		switch (this) {
		case LOWERCASE:
			char c = 'a';
			StringBuilder chars = new StringBuilder();
			chars.append(c);

			for (int i = 1; i <= num; ++i) {
				chars.setCharAt(chars.length() - 1, c);
				titles.add(chars.toString());

				if (c == 'z') {
					chars = inflate(chars);
					c = 'a';
				} else {
					++c;
				}
			}
			break;
		case UPPERCASE:
			char C = 'A';
			StringBuilder CHARS = new StringBuilder();
			CHARS.append(C);

			for (int i = 1; i <= num; ++i) {
				CHARS.setCharAt(CHARS.length() - 1, C);
				titles.add(CHARS.toString());

				if (C == 'Z') {
					CHARS = inflate(CHARS);
					C = 'A';
				} else {
					++C;
				}
			}
			break;
		default:
			for (int i = 1; i <= num; ++i) {
				titles.add(Integer.toString(i));
			}
		}

		return titles.toArray(new String[titles.size()]);
	}

	private StringBuilder inflate(StringBuilder str) {
		char a = (this == CellNameType.LOWERCASE) ? 'a' : 'A';
		char z = (this == CellNameType.LOWERCASE) ? 'z' : 'Z';

		int end = str.length() - 1;

		if (str.charAt(0) == z) {
			for (int i = 0; i < str.length(); ++i) {
				str.setCharAt(i, a);
			}

			return new StringBuilder(str + Character.toString(a));
		}

		if (str.charAt(end) == z) {
			String inflated = inflate(new StringBuilder(str.substring(0, end)))
					.append(a).toString();

			return new StringBuilder(inflated);
		}

		char next = str.charAt(end);
		++next;
		str.setCharAt(end, next);

		return str;
	}
}
