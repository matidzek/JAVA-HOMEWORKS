
package zad2;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {

      Function<String, List<String>> flines = (n -> {try {
          return Files.readAllLines(Paths.get(n));
      } catch (IOException e) {
          return Collections.emptyList();
      }
      });
      Function<List<String>, String> join = (n -> {
          StringBuilder builder = new StringBuilder();
          n.forEach(l -> builder.append(l));
          return builder.toString();
      });
      Function<String, List<Integer>> collectInts = (n->
          Pattern.compile("\\d+").matcher(n).results().map(a -> Integer.valueOf(a.group())).collect(Collectors.toList()));
      Function<List<Integer>, Integer> sum = (n -> n.stream().mapToInt(Integer::intValue).sum());

    String fname = System.getProperty("user.home") + "/LamComFile.txt";
    InputConverter<String> fileConv = new InputConverter<>(fname);
    List<String> lines = fileConv.convertBy(flines);
    String text = fileConv.convertBy(flines, join);
    List<Integer> ints = fileConv.convertBy(flines, join, collectInts);
    Integer sumints = fileConv.convertBy(flines, join, collectInts, sum);

    System.out.println(lines);
    System.out.println(text);
    System.out.println(ints);
    System.out.println(sumints);

    List<String> arglist = Arrays.asList(args);
    InputConverter<List<String>> slistConv = new InputConverter<>(arglist);
    sumints = slistConv.convertBy(join, collectInts, sum);
    System.out.println(sumints);

  }
}
