package lzkj.Spark_Study.Spark_Core_Java;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

import lzkj.Spark_Study.Common_Remember.Use_Tools;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

/**
 * @Author: wangya
 * @Ime: 2018/6/7 22:40
 * @Version: 1.0
 */
public class Sparksql_Java {
    private static SparkSession spark =null;
    static{
        spark= SparkSession.builder().appName("Sparksql_Java").master("local[2]").getOrCreate();
    }

    public static void main(String[] args) {

    }

    /**
     * Creating Datasets
     */
    public static class Person implements Serializable {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
    public static void create_Dataset(SparkSession spark){

        /**
         * 创建基本Dataset
         */
        // Create an instance of a Bean class
        Person person = new Person();
        person.setName("Andy");
        person.setAge(32);
        // Encoders are created for Java beans
        Encoder<Person> personEncoder = Encoders.bean(Person.class);
        Dataset<Person> javaBeanDS = spark.createDataset(
            Collections.singletonList(person),
            personEncoder
        );
        javaBeanDS.show();
        // +---+----+
        // |age|name|
        // +---+----+
        // | 32|Andy|
        // +---+----+

        // Encoders for most common types are provided in class Encoders
        Encoder<Integer> integerEncoder = Encoders.INT();
        Dataset<Integer> primitiveDS = spark.createDataset(Arrays.asList(1, 2, 3), integerEncoder);
        Dataset<Integer> transformedDS = primitiveDS.map(
            (MapFunction<Integer, Integer>)value -> value + 1,
            integerEncoder);
        transformedDS.collect(); // Returns [2, 3, 4]

        primitiveDS.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) throws Exception {
                return integer+1;
            }
        },integerEncoder);


        // DataFrames can be converted to a Dataset by providing a class. Mapping based on name
        String path = Use_Tools.spark_resource_Java()+"/people.json";
        Dataset<Person> peopleDS = spark.read().json(path).as(personEncoder);
        peopleDS.show();
        // +----+-------+
        // | age|   name|
        // +----+-------+
        // |null|Michael|
        // |  30|   Andy|
        // |  19| Justin|
        // +----+-------+
    }
}

