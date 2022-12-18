use std::cmp::Ordering;

pub fn day13_1(str: &str) {
    let mut count = 1;
    let mut lines = str.lines();

    let mut sum = 0;

    loop {
        let next = lines.next();
        if let None = next {
            break;
        }
        let first = next.unwrap();
        if first.is_empty() {
            break;
        }

        let second = lines.next().unwrap();
        lines.next();


        println!("\ncomparing...");
        println!("{}", first);
        println!("{}", second);
        let mut comparer = Comparer {
            left: &first.chars().collect(),
            left_i: 0,
            right: &second.chars().collect(),
            right_i: 0,
        };

        if comparer.compare_list().unwrap() {
            println!("yes!");
            sum += count;
        }
        count += 1;
    }

    println!("{sum}")
}

pub fn day13_2(str: &str) {
    let mut lines: Vec<&str> = str.lines().filter(|l| !l.is_empty()).collect();
    lines.push("[[2]]");
    lines.push("[[6]]");

    let mut lines: Vec<Line> = lines.iter().map(|l| Line { line: l }).collect();

    lines.sort();

    let mut first = 0;
    let mut second = 0;
    let mut i = 1;
    for line in lines {
        if line.line.eq("[[2]]") {
            first = i;
        }
        if line.line.eq("[[6]]") {
            second = i;
        }
        i += 1;
    }

    println!("{}", first * second);
}

struct Line<'a> {
    line: &'a str,
}

impl<'a> PartialEq<Self> for Line<'a> {
    fn eq(&self, other: &Self) -> bool {
        self.cmp(other) == Ordering::Equal
    }
}

impl<'a> PartialOrd<Self> for Line<'a> {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

impl<'a> Eq for Line<'a> {}

impl<'a> Ord for Line<'a> {
    fn cmp(&self, other: &Self) -> Ordering {
        let mut comparer = Comparer {
            left: &self.line.chars().collect(),
            left_i: 0,
            right: &other.line.chars().collect(),
            right_i: 0,
        };
        return match comparer.compare_list() {
            None => {
                Ordering::Equal
            }
            Some(true) => {
                Ordering::Less
            }
            Some(false) => {
                Ordering::Greater
            }
        }
    }
}

struct Comparer<'a> {
    left: &'a Vec<char>,
    left_i: usize,
    right: &'a Vec<char>,
    right_i: usize,
}

impl<'a> Comparer<'a> {
    fn compare_list(&mut self) -> Option<bool> {
        if self.left[self.left_i] != '[' || self.right[self.right_i] != '[' {
            panic!("not the beginning of a list!");
        }

        self.left_i += 1;
        self.right_i += 1;

        loop {
            if self.left[self.left_i] == ',' {
                self.left_i += 1;
            }
            if self.right[self.right_i] == ',' {
                self.right_i += 1;
            }
            if self.left[self.left_i] == '[' && self.right[self.right_i] == '[' {
                let result = self.compare_list();
                match result {
                    Some(b) => {
                        return Some(b);
                    }
                    None => {
                        //nothing
                    }
                }
            } else if self.left[self.left_i] == '[' {
                if self.right[self.right_i] == ']' {
                    return Some(false);
                }
                let result = self.read_int(self.right, self.right_i);
                if let None = result {
                    return Some(false);
                }
                let (int, i) = result.unwrap();
                self.right_i = i;
                let new_list = format!("[{}]", int);
                let mut comparer = Comparer {
                    left: self.left,
                    left_i: self.left_i,
                    right: &new_list.chars().collect(),
                    right_i: 0,
                };
                let result = comparer.compare_list();
                match result {
                    Some(b) => {
                        return Some(b);
                    }
                    None => {
                        //nothing
                    }
                }
            } else if self.right[self.right_i] == '[' {
                if self.left[self.left_i] == ']' {
                    return Some(true);
                }
                let result = self.read_int(self.left, self.left_i);
                if let None = result {
                    return Some(true);
                }
                let (int, i) = result.unwrap();
                self.left_i = i;
                let new_list = format!("[{}]", int);
                let mut comparer = Comparer {
                    left: &new_list.chars().collect(),
                    left_i: 0,
                    right: self.right,
                    right_i: self.right_i,
                };
                let result = comparer.compare_list();
                match result {
                    Some(b) => {
                        return Some(b);
                    }
                    None => {
                        //nothing
                    }
                }
            } else if self.left[self.left_i] == ']' && self.right[self.right_i] == ']' {
                self.left_i += 1;
                self.right_i += 1;
                return None;
            } else if self.left[self.left_i] == ']' || self.right[self.right_i] == ']' {
                return if self.left[self.left_i] == ']' {
                    Some(true)
                } else {
                    Some(false)
                };
            } else {
                let result_left = self.read_int(self.left, self.left_i);
                let result_right = self.read_int(self.right, self.right_i);
                if let None = result_left {
                    return if let None = result_right {
                        None
                    } else {
                        Some(false)
                    };
                }
                if let None = result_right {
                    return Some(true);
                }
                let (left, new_left) = result_left.unwrap();
                let (right, new_right) = result_right.unwrap();
                self.left_i = new_left;
                self.right_i = new_right;
                // println!("comparing {} > {}", left, right);
                if left > right {
                    return Some(false);
                }
                if right > left {
                    return Some(true);
                }
            }
        }
    }

    fn read_int(&self, packet: &Vec<char>, i: usize) -> Option<(i32, usize)> {
        if !packet[i].is_ascii_digit() {
            panic!("not a digit: {} at i: {}", packet[i], i);
        }
        let mut end = i;
        let mut number = String::new();
        loop {
            if !packet[end].is_ascii_digit() {
                break;
            }
            number.push(packet[end]);
            end += 1;
        }
        if number.is_empty() {
            return None;
        }
        if packet[end] == ',' {
            end += 1;
        }

        return Some((number.parse().unwrap(), end));
    }
}
