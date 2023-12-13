#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day13;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in13_ex.txt").unwrap();
    let str = fs::read_to_string("in/in13.txt").unwrap();
    day13::day13(&str.trim());
    println!("took {:?}", before.elapsed());
}