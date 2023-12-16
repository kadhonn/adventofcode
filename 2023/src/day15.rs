use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day15(str: &str) {
    let mut sum = 0;
    for string in str.split(",") {
        let mut hash = 0;
        for char in string.chars() {
            hash += char as i32;
            hash *= 17;
            hash = hash % 256;
        }
        // println!("{hash}");
        sum += hash;
    }

    println!("{sum}");
}