#![allow(dead_code)]

extern crate core;

use std::fs;

mod day1;

fn main() {
    // let str = fs::read_to_string("in/in1_ex.txt").unwrap();
    let str = fs::read_to_string("in/in1.txt").unwrap();
    day1::day1_1(&str);
}