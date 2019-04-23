package com.csv.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.dozer.DozerConverter;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.dozer.CsvDozerBeanReader;
import org.supercsv.io.dozer.ICsvDozerBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import com.csv.entity.Fields;
import com.csv.entity.IssueComment;
import com.csv.entity.Progress;
import com.csv.entity.User;
import com.csv.repo.CsvRepository;

@Service
public class CsvService {
	private final String FILE_PATH = getClass().getClassLoader().getResource("fields.csv").getPath();
	 
	@Autowired
	private CsvRepository dao;

	public List<Fields> getUrl() throws Exception {
		List<Fields> list = processInputFile(FILE_PATH);
		dao.saveAll(list);
	 	 return list;
	}

	public List<Fields> processInputFile(String inputFilePath) {
    List<Fields> inputList = new ArrayList<Fields>();
    try{
      File inputF = new File(inputFilePath);
      InputStream inputFS = new FileInputStream(inputF);
      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
      inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
      br.close();
    } catch (IOException e) {}
    return inputList ;
}
	
	 private Function<String, Fields> mapToItem = (line) -> {
		  String[] p =line.split(","); 
		  Fields item = new Fields();
		  Progress progress = new Progress();
		  item.setId(Integer.parseInt(p[0])); 
		  item.setAggregatetimeestimate(p[1]);
		  progress.setAggregateprogress(Long.parseLong(p[2]));
		  item.setAggregateprogress(progress);;
		  return item; 
	  };
}


