# 这里记录了 Spark_Core 中运行中出现的错误！

## spark 算子

### 未序列化错误
* 错误再现

```scala
   class MyClass  {
      val field = "Hello"
      def doStuff(rdd: RDD[String]): RDD[String] = { rdd.map(x => field + x) }
    }
    // 下面这种情况会报错
    /**
      *  Exception in thread "main" org.apache.spark.SparkException: Task not serializable
      *
      *  因为任务要传到集群上面，而每一个map中都调用了外部类的一个属性字段，就相当于每次都调用了外部类，但是外部类没有序列化就会有此类错误！
      *  
      */
```

解决：
* 参考博客：

[未序列化](这里参考过的博客：http://www.cnblogs.com/zwCHAN/p/4305156.html)

```scala
    def doStuff(rdd: RDD[String]): RDD[String] = {
      val field_ = this.field
      rdd.map(x => field_ + x)
    }
    //调用
    new MyClass().doStuff(SourceLine).foreach(println(_))
```

---

# 已解决

### spark pipeTest 主类找不到

spark-submit --class lzkj.Spark_Study.Spark_Core.pipeTest.PipeTest  --master yarn  /shenyabo/HadoopTest/jars/pipe.jar  /shenyabo/HadoopTest/shell/echo.sh

## 原因，使用idea 原生大jar包方式有问题，大小100M ，使用插件打包方式不会出现这个问题了，jar包大小有 几百kb了


解决方案，在prom文件中添加所需打包插件的配置

```xml
 <build>
        <defaultGoal>compile</defaultGoal>
        <plugins>
            <!--Java代码编译插件-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <executions>
                    <execution>
                        <id>default-compile</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <encoding>UTF-8</encoding>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!--Scala代码编译插件-->
            <plugin>
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>3.3.1</version>
                <executions>
                    <execution>
                        <id>scala-compile-first</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>add-source</goal>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>scala-test-compile</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!--依赖JAR提取插件-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <outputDirectory>${project.build.directory}/lib</outputDirectory>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!--生成主清单属性-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <mainClass>lzkj.Spark_Study.Spark_Core.Accumulators.MyOwnAccumulator_2.MyAccumulator_Test</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
```

## 

# 未解决




### RepartitionAndSortWithinPartitions 的实现 
