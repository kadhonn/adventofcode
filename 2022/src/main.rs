#![allow(dead_code)]

use std::fs;

mod day2;

fn main() {
    let str = fs::read_to_string("in/in2").unwrap();
    day2::day2_2(str.trim());
}