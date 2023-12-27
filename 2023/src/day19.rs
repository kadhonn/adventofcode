use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::f32::consts::E;
use std::i64::MAX;
use std::io::BufRead;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use priority_queue::PriorityQueue;
use regex::Regex;


struct Check<'a> {
    field_name: Option<&'a str>,
    operator: Operator,
    value: Option<i32>,
    result: &'a str,
}

#[derive(PartialEq)]
enum Operator {
    NONE,
    LESS,
    GREATER,
}

pub fn day19(str: &str) {
    let split = str.split("\r\n\r\n").collect::<Vec<&str>>();

    let mut workflows = HashMap::new();
    for workflow_string in split[0].lines() {
        let workflow_split = workflow_string.split(|c| c == '{' || c == '}').collect::<Vec<&str>>();
        let name = workflow_split[0];
        let checks: Vec<Check> = workflow_split[1].split(',').map(|check| parse_check(check)).collect();
        workflows.insert(name, checks);
    }

    let mut sum = 0;
    for part_string in split[1].lines() {
        let part = parse_part(part_string);
        if is_accepted(&workflows, &part) {
            sum += part.values().sum::<i32>();
        }
    }
    println!("{sum}");
}

fn is_accepted(workflows: &HashMap<&str, Vec<Check>>, part: &HashMap<&str, i32>) -> bool {
    let mut current = "in";
    'workflows: while true {
        if current == "A" {
            return true;
        }
        if current == "R" {
            return false;
        }
        let workflow = &workflows[current];
        for check in workflow {
            if check.operator == Operator::NONE {
                current = check.result;
                continue 'workflows;
            }
            if check.operator == Operator::GREATER {
                if part[check.field_name.unwrap()] > check.value.unwrap() {
                    current = check.result;
                    continue 'workflows;
                }
            }
            if check.operator == Operator::LESS {
                if part[check.field_name.unwrap()] < check.value.unwrap() {
                    current = check.result;
                    continue 'workflows;
                }
            }
        }
    }
    panic!("wat")
}

fn parse_part(part_string: &str) -> HashMap<&str, i32> {
    let substring = &part_string[1..part_string.len() - 1];
    let mut part = HashMap::new();
    for prop in substring.split(',') {
        let split: Vec<&str> = prop.split('=').collect();
        part.insert(split[0], split[1].parse().unwrap());
    }
    return part;
}

fn parse_check(check_string: &str) -> Check {
    let operator = if check_string.contains('<') {
        Operator::LESS
    } else if check_string.contains('>') {
        Operator::GREATER
    } else {
        return Check {
            field_name: None,
            operator: Operator::NONE,
            value: None,
            result: check_string,
        };
    };

    let split = check_string.split(|c| c == '>' || c == '<' || c == ':').collect::<Vec<&str>>();
    return Check {
        field_name: Some(split[0]),
        operator: operator,
        value: Some(split[1].parse::<i32>().unwrap()),
        result: split[2],
    };
}