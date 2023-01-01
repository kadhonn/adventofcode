const RULES: [((char, char), (char, char)); 15] = [
    (('=', '='), ('-', '1')),
    (('=', '-'), ('-', '2')),
    (('=', '0'), ('0', '=')),
    (('=', '1'), ('0', '-')),
    (('=', '2'), ('0', '0')),
    (('-', '-'), ('0', '=')),
    (('-', '0'), ('0', '-')),
    (('-', '1'), ('0', '0')),
    (('-', '2'), ('0', '1')),
    (('0', '0'), ('0', '0')),
    (('0', '1'), ('0', '1')),
    (('0', '2'), ('0', '2')),
    (('1', '1'), ('0', '2')),
    (('1', '2'), ('1', '=')),
    (('2', '2'), ('1', '-')),
];

pub fn day25_1(str: &str) {
    let mut sum = "0".to_string();
    for line in str.trim().lines() {
        sum = add(&sum, line);
    }
    println!("{sum}");
}

fn add(n1: &str, n2: &str) -> String {
    let n1: Vec<char> = n1.chars().collect();
    let n2: Vec<char> = n2.chars().collect();
    let mut result = String::new();

    let mut carry = '0';
    for i in 0..(n1.len().max(n2.len())) {
        let d1 = if i >= n1.len() { '0' } else { n1[n1.len() - 1 - i] };
        let d2 = if i >= n2.len() { '0' } else { n2[n2.len() - 1 - i] };

        let (first_carry, new_digit) = add_digits(d1, d2);
        let (second_carry, new_digit) = add_digits(new_digit, carry);

        let (wat, new_carry) = add_digits(first_carry, second_carry);
        if wat != '0' {
            panic!("uh oh: {}", wat);
        }
        carry = new_carry;
        result.insert(0, new_digit);
    }
    if carry != '0' {
        result.insert(0, carry);
    }

    result
}

fn add_digits(d1: char, d2: char) -> (char, char) {
    for rule in RULES {
        if rule.0 == (d1, d2) || rule.0 == (d2, d1) {
            return rule.1;
        }
    }
    panic!("no rule found for {} and {}", d1, d2);
}