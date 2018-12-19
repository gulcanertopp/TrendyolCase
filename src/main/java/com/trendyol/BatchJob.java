/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trendyol;

import com.trendyol.model.Product;
import com.trendyol.model.Result;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import java.io.File;

/**
 * Skeleton for a Flink Batch Job.
 *
 * <p>For a tutorial how to write a Flink batch application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution,
 * change the main class in the POM.xml file to this class (simply search for 'mainClass')
 * and run 'mvn clean package' on the command line.
 */
public class BatchJob {

	public static void main(String[] args) throws Exception {
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		BatchTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(env);
		final ParameterTool params = ParameterTool.fromArgs(args);
		File currentDirFile = new File("");
		String file = "";

		file = currentDirFile.getAbsolutePath() + "/src/main/resources/file";

		DataSet<Product> csvInput = env
				.readCsvFile(params.get("input"))
				.ignoreFirstLine()
				.fieldDelimiter("|")
				.pojoType(Product.class, "date", "productId", "eventName", "userId");

		Table products = tableEnv.fromDataSet(csvInput);
		tableEnv.registerTable("products", products);

		//Case 1 Begin
		Table groupedByProductId = tableEnv.sql("SELECT productId , count(*) as cnt  FROM products group by productId order by cnt desc");
		DataSet<Result.ResultGroupProduct> resultGroupProduct = tableEnv.toDataSet(groupedByProductId, Result.ResultGroupProduct.class);
		resultGroupProduct.writeAsText(params.get("output") + "/resultGroupProduct.txt").setParallelism(1);
		//Case 1 End

		//Case 2 Begin
		Table groupedByEventName = tableEnv.sql("SELECT eventName , count(*) as cnt  FROM products group by eventName  order by cnt desc");
		DataSet<Result.ResultGroupEvent> resultGroupEventName = tableEnv.toDataSet(groupedByEventName, Result.ResultGroupEvent.class);
		resultGroupEventName.writeAsText(params.get("output") + "/resultGroupEventName.txt").setParallelism(1);
		//Case 2 End

		//Case 3 Begin
		Table groupedFullfilledEvents = tableEnv.sql("SELECT  p.userId,count(*) as cnt from products p " +
				"inner join (select pv.userId from products pv where pv.eventName='view' group by userId) ppv on ppv.userId = p.userId " +
				"inner join (select pa.userId from products pa where pa.eventName='add' group by userId) ppa on ppa.userId = p.userId " +
				"inner join (select pr.userId from products pr where pr.eventName='remove' group by userId) ppr on ppr.userId = p.userId " +
				"inner join (select pc.userId from products pc where pc.eventName='click' group by userId) ppc on ppc.userId = p.userId group by p.userId order by cnt desc limit 5");
		DataSet<Result.ResultFullfilledEvents> resultFullfilledEvents = tableEnv.toDataSet(groupedFullfilledEvents, Result.ResultFullfilledEvents.class);
		resultFullfilledEvents.writeAsText(params.get("output")  + "/resultFullfilledEvents.txt").setParallelism(1);
		//Case 3 End

		//Case 4 Begin
		Table groupedByEventNameUser47 = tableEnv.sql("SELECT eventName,count(*) as cnt from products where userId = '47' group by eventName ");
		DataSet<Result.ResultGroupEvent> resultFullfilledEventsUser47 = tableEnv.toDataSet(groupedByEventNameUser47, Result.ResultGroupEvent.class);
		resultFullfilledEventsUser47.writeAsText(params.get("output") + "/resultFullfilledEventsUser47.txt").setParallelism(1);
		//Case 4 End

		//Case 5 Begin
		Table groupedByProductIdUser47 = tableEnv.sql("SELECT productId from products where userId = '47'");
		DataSet<Result.ResultFullfilledProductId> resultFullfilledProductIdUser47 = tableEnv.toDataSet(groupedByProductIdUser47, Result.ResultFullfilledProductId.class);
		resultFullfilledProductIdUser47.writeAsText(params.get("output") +"/resultFullfilledProductIdUser47.txt").setParallelism(1);
		//Case 5 End

		env.execute("Flink Batch Java API Skeleton");

	}
}
