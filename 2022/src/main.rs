#![allow(dead_code)]

extern crate core;

use std::fs;

mod day23;

fn main() {
    // let str = fs::read_to_string("in/in23_ex.txt").unwrap();
    let str = fs::read_to_string("in/in23.txt").unwrap();
    day23::day23_1(&str);
}