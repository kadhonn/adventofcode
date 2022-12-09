#![allow(dead_code)]

use std::fs;

mod day6;

fn main() {
    // let str = fs::read_to_string("in/in6_ex.txt").unwrap();
    let str = fs::read_to_string("in/in6.txt").unwrap();
    day6::day6_1(&str.trim());
}