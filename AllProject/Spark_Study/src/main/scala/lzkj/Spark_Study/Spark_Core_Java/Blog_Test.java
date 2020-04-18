package lzkj.Spark_Study.Spark_Core_Java;

import java.awt.geom.Line2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import lzkj.Spark_Study.Common_Remember.Use_Tools;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;


/**
 * @Author: yabo
 * @Ime: 2018/6/7 16:23
 * @Version: 1.0
 */
public class Blog_Test {
    private static JavaSparkContext jsc = null;
    private static SparkSession spark = null;

    static {
        jsc = new JavaSparkContext(new SparkConf().setAppName("Blog_Test").setMaster("local[2]"));
        spark = SparkSession.builder().appName("Blog_Test").master("local[2]").getOrCreate();
    }

    public static void main(String[] args) {

        demo01_map(jsc);
    }

    /**
     * 1.javaAPI的使用
     */
    public static void demo01_map(JavaSparkContext jsc) {
        final JavaRDD<String> data = jsc.textFile(Use_Tools.spark_resource_Java() + "/Data.md");
        final JavaRDD<String> data_world = jsc.textFile(Use_Tools.spark_resource_Java() + "/world.txt");

        //　1. 使用ｌａｍｂｄａ　表达式

        final JavaRDD<String> line_length_count = data.map(s -> s.toString() + s.length());
        line_length_count.filter(x -> {
            if (x.toString().endsWith("0")) {
                return false;
            } else {
                return true;
            }
        }).foreach(x -> System.out.println(x));

        // 2. Function 用到map中输入一个输出一个
        final JavaRDD<Integer> line_length = data.map(new Function<String, Integer>() {
            @Override
            public Integer call(String s) throws Exception {
                return s.length();
            }
        });
        line_length.filter(x -> x != 0).foreach(x -> System.out.println(x.toString()));

        // 3. 自定义类
    /*
     class GetLength implements Function<String, int> {
            public Inter call(String s) { return s.length(); }
        }

        JavaDStream<String> lineLengths = lines.map(new GetLength())
        */

        /**
         * reduce 的使用
         */

        // 1. lambda 表达式
        final Integer reduced = line_length.reduce((a, b) -> (a + b));

        // Function2 表示
        final Integer Function_reduced = line_length.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        /**
         * reducebykey 的使用
         */

        System.out.println("reducebykey 使用");
        // maptoPair 把一行当作一个单词
        /**
         (world java,1)
         (,1)
         (world count,1)
         (php 2,null)
         (java,1)
         */
       /* final JavaPairRDD<String, Integer> tupRDD = data_world.mapToPair(
            x -> new Tuple2<String, Integer>(x, 1));*/

        final JavaPairRDD<String, Integer> tupRDD = data_world.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2(s, 1);
            }
        });

        tupRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer i1, Integer i2) throws Exception {
                return i1 + i2;
            }
        }).foreach(x -> System.out.println(x.toString()));

        /**
         * mapPartitions
         */


        final JavaRDD<Tuple2<String, String>> tuple2JavaRDD = jsc.textFile(Use_Tools.spark_resource_Java() +"/world_ip.txt").mapPartitions(parts -> {
            List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
            while (parts.hasNext()) {
                String msg = parts.next();
                String ip = msg.split(" ")[0];
                String domain = msg.split(" ")[1];
                list.add(new Tuple2<String, String>(ip, domain));
            };
            return list.iterator();
        });

        tuple2JavaRDD.collect().forEach(x-> System.out.println(x.toString()));
        /*        (1,城市)
                (2,小明)
                (3,你的)
                (4,我的)*/

        // FlatMapFunction[T, R]
        final JavaRDD<Tuple2<String, String>> tuple2JavaRDD_2 = jsc.textFile(
            Use_Tools.spark_resource_Java() + "/world_ip.txt").mapPartitions(
            new FlatMapFunction<Iterator<String>, Tuple2<String, String>>() {
                @Override
                public Iterator<Tuple2<String, String>> call(Iterator<String> s) throws Exception {
                    List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
                    while (s.hasNext()) {
                        final String lines = s.next();
                        String ip = lines.split(" ")[0];
                        String main = lines.split(" ")[1];
                        list.add(new Tuple2<String, String>(ip, main));
                    }
                    return list.iterator();
                }
            }
        );

        // flatMap

        // 远播客错误示范
/*        JavaDStream<String> words = lines.flatMap(x -> Lists.newArrayList(x.split(" ")));

        1

        Implement the Function interfaces

        // FlatMapFunction[T, R]
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            // call(t: T): Iterator[R]
            public Iterable<String> call(String x) {
                return Lists.newArrayList(SPACE.split(x));
            }
        });
        */
        // 下面一行不可以
        //data_world.flatMap(x-> Lists.newArrayList(x.split(" "))).iterator());

        // flatmap
        final JavaRDD<String> stringJavaRDD1 = data_world.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
        final JavaRDD<String> stringJavaRDD = data_world.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });

        System.out.println("flatmap 切分-----------------------------");
        //stringJavaRDD1.foreach(x-> System.out.println(x.toString()));
        final JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = stringJavaRDD1.mapToPair(
            x -> new Tuple2<String, Integer>(x, 1));
        final JavaPairRDD<String, Integer> reduceBykeyed = stringIntegerJavaPairRDD.reduceByKey(
            (a, b) -> a + b);

        System.out.println("reduceBykeed.collect.foreach--------------------");
        reduceBykeyed.collect().forEach(x-> System.out.println(x.toString()));


        /**
         * flatMapToPair
         */

        // lamba 表达式

        final JavaPairRDD<String, String> stringStringJavaPairRDD1 = data_world.flatMapToPair(x -> {
            final String[] line = x.split(" ");
            List<Tuple2<String, String>> list = new ArrayList<>();
            list.add(new Tuple2<String, String>(line[0].toString(), line[1].toString()));
            return list.iterator();
        });
        stringStringJavaPairRDD1.collect().forEach(x -> System.out.println(x.toString()));

        // PairFlatMapFunction
        final JavaPairRDD<String, String> stringStringJavaPairRDD = data_world.flatMapToPair(
            new PairFlatMapFunction<String, String, String>() {
                List<Tuple2<String, String>> list = new ArrayList<>();

                @Override
                public Iterator<Tuple2<String, String>> call(String s) throws Exception {
                    final String[] array = s.split(" ");

                    list.add(new Tuple2<String, String>(array[0].toString(), array[1].toString()));
                    return list.iterator();
                }
            });
    }

}
