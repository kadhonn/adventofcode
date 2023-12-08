#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day8;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in8_ex.txt").unwrap();
    let str = fs::read_to_string("in/in8.txt").unwrap();
    day8::day8(&str.trim());
    println!("took {:?}", before.elapsed());
}