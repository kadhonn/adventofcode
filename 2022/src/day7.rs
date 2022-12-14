use std::collections::HashMap;
use std::fmt::{Debug};

#[derive(Debug)]
enum FSEntry<'a> {
    Dir(Vec<&'a str>, Vec<FSEntry<'a>>),
    File(&'a str, i32),
}

pub fn day7_1(str: &str) {
    let map: HashMap<String, FSEntry> = parse_input(str);
    dbg!(&map);

    let mut sum = vec!(0);
    calc_sum(&map, map.get_key_value("/").unwrap(), &mut sum);

    println!("sum: {}", sum[0]);
}

pub fn day7_2(str: &str) {
    let map: HashMap<String, FSEntry> = parse_input(str);
    dbg!(&map);

    let mut sum = vec!(0);
    let all = calc_sum(&map, map.get_key_value("/").unwrap(), &mut sum);

    let wanted = 30000000 - (70000000 - all);

    let mut min = 70000000;
    let mut min_dir:Option<&str> = None;

    for (name, _) in &map {
        let dir_size = calc_sum(&map, map.get_key_value(name.as_str()).unwrap(), &mut sum);
        if dir_size > wanted && dir_size < min {
            min = dir_size;
            min_dir = Option::Some(name);
        }
    }

    println!("min: {}", min);
    println!("min_dir: {}", min_dir.unwrap());
}

fn calc_sum(map: &HashMap<String, FSEntry>, entry: (&String, &FSEntry), sum: &mut Vec<i32>) -> i32 {
    let current_dir = entry.0;
    let entry = entry.1;
    match entry {
        FSEntry::File(_, _) => panic!("in the disco"),
        FSEntry::Dir(dirs, files) => {
            let mut total_size = 0;
            for dir in dirs {
                let mut new_dir_name = current_dir.to_string();
                if new_dir_name.len() > 1 {
                    new_dir_name.push_str("/");
                }
                new_dir_name.push_str(dir);
                println!("calling folder {}", new_dir_name);
                total_size += calc_sum(map, map.get_key_value(new_dir_name.as_str()).unwrap(), sum);
            }

            for file in files {
                match file {
                    FSEntry::File(_, size) => total_size += size,
                    FSEntry::Dir(_, _) => panic!("at the disco")
                }
            }

            println!("size of dir {} is {}", current_dir, total_size);
            if total_size <= 100000 {
                sum[0] += total_size;
            }

            total_size
        }
    }
}

fn parse_input(str: &str) -> HashMap<String, FSEntry> {
    let mut map: HashMap<String, FSEntry> = HashMap::new();

    let mut current_dir: Vec<&str> = vec!();
    let split: Vec<&str> = str.split("\r\n").collect();
    let mut i = 0 as usize;
    while i < split.len() {
        let line = split[i];
        i += 1;

        if !line.starts_with("$ ") {
            panic!("line starts with unexpected token: {line}");
        }
        let mut command = line[2..].split(" ");
        let cmd_name = command.next().unwrap();
        match cmd_name {
            "cd" => {
                let dir = command.next().unwrap();
                match dir {
                    ".." => {
                        current_dir.pop();
                    }
                    "/" => {
                        current_dir.clear();
                    }
                    other => {
                        current_dir.push(other);
                    }
                }
            }
            "ls" => {
                let mut new_dirs: Vec<&str> = vec!();
                let mut new_files: Vec<FSEntry> = vec!();
                while i < split.len() {
                    let dir_line = split[i];
                    if dir_line.starts_with("$ ") {
                        break;
                    }
                    i += 1;
                    let mut dir_split = dir_line.split(" ");
                    match dir_split.next().unwrap() {
                        "dir" => {
                            let dir_name = dir_split.next().unwrap();
                            new_dirs.push(dir_name);
                        }
                        other => {
                            let size: i32 = other.parse().unwrap();
                            let file_name = dir_split.next().unwrap();
                            new_files.push(FSEntry::File(file_name, size));
                        }
                    }
                }
                let new_dir = FSEntry::Dir(new_dirs, new_files);
                let mut new_dir_name = String::from("/");
                new_dir_name.push_str(current_dir.join("/").as_str());
                map.insert(new_dir_name, new_dir);
            }
            other => {
                panic!("invalid command: {other}");
            }
        }
    }
    return map;
}