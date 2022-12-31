pub fn day20_1(str: &String) {
    let numbers: Vec<i64> = str.trim().lines().map(|l| { l.parse().unwrap() }).map(|a: i64| { 811589153 * a }).collect();
    let mut dll = make_dll(numbers.len());

    // print_something(&numbers, &dll);
    for _j in 0..10 {
        for i in 0..numbers.len() {
            let steps = numbers[i] % (numbers.len() as i64 - 1);
            if steps == 0 {
                continue;
            }
            let mut current = i;
            {
                let current_pointers = dll[i];
                dll[current_pointers.0].1 = current_pointers.1;
                dll[current_pointers.1].0 = current_pointers.0;
            }
            if steps > 0 {
                for _ in 0..steps {
                    current = dll[current].1;
                }
            } else {
                for _ in steps..=0 {
                    current = dll[current].0;
                }
            }
            let next = dll[current].1;
            dll[current].1 = i;
            dll[next].0 = i;
            dll[i].0 = current;
            dll[i].1 = next;

            // print_something(&numbers, &dll);
        }
    }

    let mut zero_i = 0;
    for i in 0..numbers.len() {
        if numbers[i] == 0 {
            zero_i = i;
            break;
        }
    }

    let mut sum = 0;
    let mut current = zero_i;
    for i in 1..=3000 {
        current = dll[current].1;
        if i % 1000 == 0 {
            println!("{}", numbers[current]);
            sum += numbers[current];
        }
    }
    println!("{}", sum);
}

fn print_something(numbers: &Vec<i64>, dll: &Vec<(usize, usize)>) {
    let mut i = 0;
    let start = dll[dll[0].0];
    let mut current = start;
    print!("{} -> ", numbers[current.1]);
    current = dll[current.1];
    while start != current {
        print!("{} -> ", numbers[current.1]);
        current = dll[current.1];
        i += 1;
        if i > numbers.len() {
            panic!("loop detected");
        }
    }
    println!();
}

fn make_dll(size: usize) -> Vec<(usize, usize)> {
    let mut dll = vec![];

    for i in 0..size {
        dll.push(((i + size - 1) % size, (i + 1) % size));
    }

    dll
}