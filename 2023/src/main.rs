#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day18;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in18_ex.txt").unwrap();
    let str = fs::read_to_string("in/in18.txt").unwrap();
    day18::day18(&str.trim());
    println!("took {:?}", before.elapsed());
}