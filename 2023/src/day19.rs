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


#[derive(Copy, Clone)]
struct Check<'a> {
    field_name: Option<&'a str>,
    operator: Operator,
    value: Option<i64>,
    result: &'a str,
}

#[derive(PartialEq, Copy, Clone)]
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

    let sum = recursive_travel(&workflows, &mut vec![], "in");


    println!("{sum}");
}

fn recursive_travel<'a>(workflows: &'a HashMap<&str, Vec<Check<'a>>>, path: &mut Vec<Check<'a>>, current: &str) -> i64 {
    if current == "R" {
        return 0;
    }
    if current == "A" {
        return calc_path(path);
    }

    let mut sum = 0i64;
    let workflow = &workflows[current];
    let orig_len = path.len();
    for check in workflow {
        path.push(*check);
        sum += recursive_travel(workflows, path, check.result);
        path.remove(path.len() - 1);
        path.push(reverse(*check));
    }
    path.truncate(orig_len);

    return sum;
}

fn calc_path(path: &Vec<Check>) -> i64 {
    let mut x_min = 1;
    let mut x_max = 4000;
    let mut m_min = 1;
    let mut m_max = 4000;
    let mut a_min = 1;
    let mut a_max = 4000;
    let mut s_min = 1;
    let mut s_max = 4000;

    for check in path.iter().rev() {
        if check.operator == Operator::GREATER {
            if check.field_name.unwrap() == "x" {
                x_min = i64::max(x_min, check.value.unwrap() + 1);
            }
            if check.field_name.unwrap() == "m" {
                m_min = i64::max(m_min, check.value.unwrap() + 1);
            }
            if check.field_name.unwrap() == "a" {
                a_min = i64::max(a_min, check.value.unwrap() + 1);
            }
            if check.field_name.unwrap() == "s" {
                s_min = i64::max(s_min, check.value.unwrap() + 1);
            }
        }
        if check.operator == Operator::LESS {
            if check.field_name.unwrap() == "x" {
                x_max = i64::min(x_max, check.value.unwrap() - 1);
            }
            if check.field_name.unwrap() == "m" {
                m_max = i64::min(m_max, check.value.unwrap() - 1);
            }
            if check.field_name.unwrap() == "a" {
                a_max = i64::min(a_max, check.value.unwrap() - 1);
            }
            if check.field_name.unwrap() == "s" {
                s_max = i64::min(s_max, check.value.unwrap() - 1);
            }
        }
    }

    return i64::max(x_max - x_min + 1, 0)
        * i64::max(m_max - m_min + 1, 0)
        * i64::max(a_max - a_min + 1, 0)
        * i64::max(s_max - s_min + 1, 0);
}

fn reverse(check: Check) -> Check {
    return if check.operator == Operator::NONE {
        check
    } else {
        if check.operator == Operator::GREATER {
            Check {
                field_name: check.field_name,
                operator: Operator::LESS,
                value: Some(check.value.unwrap() + 1),
                result: check.result,
            }
        } else {
            Check {
                field_name: check.field_name,
                operator: Operator::GREATER,
                value: Some(check.value.unwrap() - 1),
                result: check.result,
            }
        }
    };
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
        value: Some(split[1].parse::<i64>().unwrap()),
        result: split[2],
    };
}