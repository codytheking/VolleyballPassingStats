//
//  OptionsViewController.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/26/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import UIKit


class OptionsViewController: UIViewController {
    
    // MARK: Properties
    
    @IBOutlet weak var numPlayersTextField: UITextField!
    @IBOutlet weak var clearAllBtn: UIButton!
    @IBOutlet weak var numPlayersStepper: UIStepper!
    
    static var clearAll = false
    static var numPlayers = 20
    static let maxNumPlayers = 20
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        numPlayersStepper.maximumValue = Double(OptionsViewController.maxNumPlayers)
        numPlayersStepper.minimumValue = 1
        numPlayersStepper.stepValue = 1
        numPlayersStepper.value = Double(OptionsViewController.maxNumPlayers)
    }

    
    // MARK: Actions
    
    @IBAction func changeNumPlayers(_ sender: UIStepper) {
        self.numPlayersTextField.text = Int(sender.value).description
        OptionsViewController.numPlayers = Int(sender.value)
    }

    @IBAction func clearAllData(_ sender: UIButton) {
        let refreshAlert = UIAlertController(title: "Refresh", message: "All data will be lost.", preferredStyle: UIAlertController.Style.alert)

        refreshAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
            OptionsViewController.clearAll = true
        }))

        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in
        }))

        present(refreshAlert, animated: true, completion: nil)
    }
    
    @IBAction func nameHyperlink(_ sender: UIButton) {
        if let url = NSURL(string: "http://codyjking.com/") {
            
            let refreshAlert = UIAlertController(title: "Open Link", message: "Go to developer's website?", preferredStyle: UIAlertController.Style.alert)

            refreshAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
                UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
            }))

            refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in
            }))

            present(refreshAlert, animated: true, completion: nil)
        }
    }
}
