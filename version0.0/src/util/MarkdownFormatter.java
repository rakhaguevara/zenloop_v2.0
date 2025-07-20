package util;

public class MarkdownFormatter {

    public static String clean(String text) {
        if (text == null)
            return "";

        // 1. Hapus heading markdown seperti "# Judul"
        text = text.replaceAll("(?m)^#+\\s*", "");

        // 2. Hapus bullet point "* " atau "- "
        text = text.replaceAll("(?m)^[-*]\\s+", "• ");

        // 3. Hapus format bold dan italic markdown secara umum
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1"); // **bold**
        text = text.replaceAll("_(.*?)_", "$1"); // _italic_
        text = text.replaceAll("\\*(.*?)\\*", "$1"); // *italic*

        // 4. Ubah tag custom menjadi unicode jika diinginkan
        text = text.replaceAll("\\[b\\](.*?)\\[/b\\]", "\u001B[1m$1\u001B[0m"); // bisa ubah ke <b>$1</b> jika HTML
        text = text.replaceAll("\\[i\\](.*?)\\[/i\\]", "\u001B[3m$1\u001B[0m"); // bisa ubah ke <i>$1</i>

        // 5. Hapus karakter berlebihan
        text = text.trim();
        return text;
    }
}
