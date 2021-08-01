
public class Exam {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }

    char firstNonRepeated(String s) {
        Map<char, int> count; // character count

        for ch in s {
            if count.containsKey(ch) {
                int value = count.get(ch);
                value += 1;
                count.put(ch, value);
            } else {
                count.put(ch, 1);
            }
        }

        for ch in count.allKeys() {
            int value = count.get(ch);
            if value == 1 {
                return ch;
            }
        }
    }

    String wordsReverse(String s) {
        String[] words = s.split(" ");
        StringBuilder sb;
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]);
            if i !=0 {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}