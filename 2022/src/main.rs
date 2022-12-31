#![allow(dead_code)]

extern crate core;

use std::fs;

mod day21;

fn main() {
    // let str = fs::read_to_string("in/in21_ex.txt").unwrap();
    let str = fs::read_to_string("in/in21.txt").unwrap();
    day21::day21_2(&str);
}