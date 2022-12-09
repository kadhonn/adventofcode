#![allow(dead_code)]

use std::fs;

mod day4;

fn main() {
    // let str = fs::read_to_string("in/in4_ex").unwrap();
    let str = fs::read_to_string("in/in4").unwrap();
    // day4::day4_1(str.trim());
    day4::day4_2(str.trim());
}