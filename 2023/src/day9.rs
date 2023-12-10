use std::cmp::{max, min, Ordering};
use std::collections::HashMap;
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day9(str: &str) {
    let mut sum = 0;

    for line in str.lines() {
        let mut sequences = vec![];
        let numbers = line.split(" ").filter(|str| !str.trim().is_empty()).map(|str| str.trim().parse::<i32>().unwrap()).collect::<Vec<i32>>();
        sequences.push(numbers);

        while true {
            let mut found_non_zero = false;
            let mut new_numbers = vec![];
            let last_numbers = sequences.last().unwrap();
            for i in 0..(last_numbers.len() - 1) {
                let new_number = last_numbers[i + 1] - last_numbers[i];
                new_numbers.push(new_number);
                if new_number != 0 {
                    found_non_zero = true;
                }
            }
            if !found_non_zero {
                break;
            }
            sequences.push(new_numbers);
        }

        let mut change = 0;
        for i in (0..sequences.len()).rev() {
            let last_numbers = &sequences[i];
            let last_number = last_numbers[0];
            change = last_number - change;
        }

        sum += change;
    }

    println!("{}", sum);
}
