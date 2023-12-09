use std::cmp::{max, min, Ordering};
use std::collections::HashMap;
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day8(str: &str) {
    let mut map = HashMap::new();
    let regex = Regex::new(r"(\w{3}) = \((\w{3}), (\w{3})\)").unwrap();

    let instructions = str.lines().next().unwrap().chars().collect::<Vec<char>>();

    for line in str.lines().skip(2) {
        for group in regex.captures_iter(line) {
            let begin = group.get(1).unwrap().as_str();
            let left = group.get(2).unwrap().as_str();
            let right = group.get(3).unwrap().as_str();
            map.insert(begin, (left, right));
        }
    }

    let mut starting_points = map.keys().filter(|k| k.ends_with("A")).map(|k| *k).collect::<Vec<&str>>();
    let mut cycles: Vec<Vec<(i32, i32)>> = vec![];
    for start_point in starting_points {
        let mut current = start_point;
        let mut current_cylces: Vec<(i32, i32)> = vec![];
        for i in 1..i32::MAX {
            let current_instruction = instructions[(i as usize - 1) % instructions.len()];
            current = if current_instruction == 'L' {
                map[current].0
            } else {
                map[current].1
            };

            if current.ends_with("Z") {
                if current_cylces.is_empty() {
                    current_cylces.push((i, i));
                } else {
                    let last_cylce = current_cylces.last().unwrap();
                    current_cylces.push((i, i - (*last_cylce).0));
                }
            }
            if current_cylces.len() == 10 {
                break;
            }
        }
        cycles.push(current_cylces);
    }

    let mut new_cycles: Vec<(BigInt, BigInt)> = cycles.iter().map(|cycle| (BigInt::from(cycle[0].1), BigInt::from(cycle[1].1))).collect();
    // let mut new_cycles = vec![(BigInt::from(2809), BigInt::from(14893)), (BigInt::from(2528), BigInt::from(22199))];
    while new_cycles.len() > 1 {
        let cycle1 = new_cycles.remove(0);
        let cycle2 = new_cycles.remove(new_cycles.len() - 1);
        let mut sum1 = cycle1.0;
        let mut sum2 = cycle2.0;
        let mut hits = vec![];
        while hits.len() != 2 {
            if sum1.eq(&sum2) {
                hits.push(sum1.clone());
                sum1 += &cycle1.1;
            } else if sum1 < sum2 {
                // sum1 += &cycle1.1;
                let mut mult = (&sum2 - &sum1) / &cycle1.1;
                if mult == BigInt::from(0) {
                    mult = BigInt::from(1);
                }
                sum1 += &mult * &cycle1.1;
            } else {
                // sum2 += &cycle2.1;
                let mut mult = (&sum1 - &sum2) / &cycle2.1;
                if mult == BigInt::from(0) {
                    mult = BigInt::from(1);
                }
                sum2 += &mult * &cycle2.1;
            }
        }
        new_cycles.push((hits[0].clone(), hits[1].clone()));
    }

    println!("{}", new_cycles[0].0);
}
