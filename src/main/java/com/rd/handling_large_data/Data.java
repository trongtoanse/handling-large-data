package com.rd.handling_large_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Data {
    private String label;
    private int rowSize;
    private List<LinkedList<String>> rows;
}
