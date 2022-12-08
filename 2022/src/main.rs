#![allow(dead_code)]

use std::fs;

mod day3;

fn main() {
    // let str = fs::read_to_string("in/in3_ex").unwrap();
    let str = fs::read_to_string("in/in3").unwrap();
    day3::day3_2(str.trim());
}