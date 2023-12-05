#![allow(dead_code)]

extern crate core;

use std::fs;

mod day2;

fn main() {
    // let str = fs::read_to_string("in/in2_ex.txt").unwrap();
    let str = fs::read_to_string("in/in2.txt").unwrap();
    day2::day2_1(&str.trim());
}